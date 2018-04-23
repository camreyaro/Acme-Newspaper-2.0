
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Customer;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CustomerServiceTest extends AbstractTest {

	// The SUT -------------------------------------

	@Autowired
	private CustomerService	customerService;


	@Test
	public void sampleDriver() {
		final Object testingData[][] = {

			{
				"customer1", null, "edit"
			}, //Un usuario puede editar sus datos personales

			{
				"customer1", IllegalArgumentException.class, "editNoTerms"
			}, //Un usuario debe aceptar los términos y condiciones

			{
				"customer1", ConstraintViolationException.class, "editNullEmail"
			}, //Un usuario debe introducir su email

			{
				"customer1", ConstraintViolationException.class, "editNullName"
			}, //Un usuario debe introducir su nombre

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (Class<?>) testingData[i][1], (String) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String username, final Class<?> expected, final String operation) {
		Class<?> caught;
		Customer customer;

		caught = null;
		try {
			super.authenticate(username);
			customer = this.customerService.findOne(this.getEntityId(username));

			if (operation.equals("edit"))
				customer.setEmailAddress("alum@alum.us");
			else if (operation.equals("editNullEmail"))
				customer.setEmailAddress(null);
			else if (operation.equals("editNullName"))
				customer.setName(null);
			else if (operation.equals("editNoTerms")) {
				customer.setHasConfirmedTerms(false);
				this.customerService.saveFromCreate(customer);
			}
			this.customerService.saveAndFlush(customer);
			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
