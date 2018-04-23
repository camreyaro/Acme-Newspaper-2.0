package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.User;

import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserServiceTest extends AbstractTest {

	// The SUT -------------------------------------

	@Autowired
	private UserService userService;
	
	@Test
	public void sampleDriver() {
		final Object testingData[][] = {
				
				{ "user1", null, "edit" }, //Un usuario puede editar sus datos personales
	
				{ "user1", IllegalArgumentException.class, "editNoTerms" }, //Un usuario debe aceptar los términos y condiciones
				
				{ "user1", ConstraintViolationException.class, "editNullEmail" }, //Un usuario debe introducir su email
				
				{ "user1", ConstraintViolationException.class, "editNullName" }, //Un usuario debe introducir su nombre
				
				};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0],
						(Class<?>) testingData[i][1], (String) testingData[i][2]);
				} catch (final Throwable oops) {
					throw new RuntimeException(oops);
				} finally {
					super.rollbackTransaction();
				}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String username, final Class<?> expected, String operation) {
		Class<?> caught;
		User user;

		caught = null;
		try {
			super.authenticate(username);
			user = userService.findOne(this.getEntityId(username));
			
			if (operation.equals("edit")) {
				user.setEmailAddress("alum@alum.us");
			}else if(operation.equals("editNullEmail")) {
				user.setEmailAddress(null);
			}else if(operation.equals("editNullName")) {
				user.setName(null);
			}else if(operation.equals("editNoTerms")) {
				user.setHasConfirmedTerms(false);
				this.userService.saveFromCreate(user);
			}
			this.userService.saveAndFlush(user);
			super.unauthenticate();
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
