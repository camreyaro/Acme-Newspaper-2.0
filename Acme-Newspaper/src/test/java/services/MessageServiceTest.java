
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Actor;
import domain.Folder;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageServiceTest extends AbstractTest {

	// The SUT -------------------------------------

	@Autowired
	private CustomerService	customerService;
	@Autowired
	private ActorService	actorService;
	@Autowired
	private FolderService	folderService;


	@Test
	public void sampleDriver() {
		final Object testingData[][] = {

			{
				"customer1", "box", null, "create"
			}, //Un usuario puede editar sus datos personales
			{
				"admin", "inbox", IllegalArgumentException.class, "create"
			}, //Un usuario puede editar sus datos personales
			{
				"admin", "box2", null, "createParent"
			}, //Un usuario puede editar sus datos personales
			{
				"admin", "inbox", IllegalArgumentException.class, "delete"
			}, //Un usuario puede editar sus datos personales
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2], (String) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String username, final String folderName, final Class<?> expected, final String operation) {
		Class<?> caught;
		final Actor customer;

		caught = null;
		try {
			super.authenticate(username);
			customer = this.actorService.findOne(this.getEntityId(username));
			final Folder f = this.folderService.create();
			f.setName(username);
			f.setParent(null);
			if (operation.equals("create"))
				this.folderService.createForUserRaiz(folderName);
			else if (operation.equals("createParent"))
				this.folderService.createForUser(folderName, this.getEntityId("folderA2"));
			else if (operation.equals("delete")) {
				final Folder fo = this.folderService.findOne(this.getEntityId("folderA2"));
				this.folderService.delete(fo);
			}
			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
