
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Agent;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AgentServiceTest extends AbstractTest {

	// The SUT -------------------------------------

	@Autowired
	private AgentService	agentService;


	@Test
	public void sampleDriver() {
		final Object testingData[][] = {

			{
				"agent1", null, "edit"
			}, //Un usuario puede editar sus datos personales

			{
				"agent1", IllegalArgumentException.class, "editNoTerms"
			}, //Un usuario debe aceptar los términos y condiciones

			{
				"agent1", ConstraintViolationException.class, "editNullEmail"
			}, //Un usuario debe introducir su email

			{
				"agent1", ConstraintViolationException.class, "editNullName"
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
		Agent agent;

		caught = null;
		try {
			super.authenticate(username);
			agent = this.agentService.findOne(this.getEntityId(username));

			if (operation.equals("edit"))
				agent.setEmailAddress("alum@alum.us");
			else if (operation.equals("editNullEmail"))
				agent.setEmailAddress(null);
			else if (operation.equals("editNullName"))
				agent.setName(null);
			else if (operation.equals("editNoTerms")) {
				agent.setHasConfirmedTerms(false);
				this.agentService.saveFromCreate(agent);
			}
			this.agentService.saveAndFlush(agent);
			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
