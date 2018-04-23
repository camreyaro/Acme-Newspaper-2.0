package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Chirp;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
	@RunWith(SpringJUnit4ClassRunner.class)
	@Transactional
public class ChirpServiceTest extends AbstractTest{
	
	@Autowired
	private ChirpService	chirpService;
	
	@Test
	public void adminDriver() {
		final Object testingData[][] = {

			{
				"admin", "chirp1", null, "delete", // Borrado correcto
				
				"user1", "chirp1", IllegalArgumentException.class, "delete", // Un user no puede borrar un artículo
				
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAdmin((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2], (String) testingData[i][3]);

	}
	// Método base para realizar test

	protected void templateAdmin(final String username, final String articleName, final Class<?> expected, String operation) {
		Class<?> caught;
		Chirp chirp;

		caught = null;
		try {
			super.authenticate(username);
			chirp = chirpService.findOne(super.getEntityId(articleName));
			
			if(operation.equals("delete")){
				chirpService.delete(chirp);
			}
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
