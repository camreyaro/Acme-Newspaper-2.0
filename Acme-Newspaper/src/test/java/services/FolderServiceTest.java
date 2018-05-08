
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Folder;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FolderServiceTest extends AbstractTest {

	// The SUT -------------------------------------

	@Autowired
	private FolderService	folderService;


	@Test
	public void sampleDriver() {
		final Object testingData[][] = {

			{
				"customer1", "Carpeta Test2", null, null, "create"
			}, //Crear una carpeta

			{
				"customer1", "Carpeta Test inbox", " inbox", null, "create"
			}, //Crear carpeta dentro de otra carpeta

			{
				"customer1", "inbox", null, IllegalArgumentException.class, "create"
			}, //Fallo al crear una carpeta con nombre normal

			{
				"customer1", "carpeta1", null, null, "delete"
			}, //borrar una carpeta

			{
				"customer1", "inbox", IllegalArgumentException.class, "delete"
			}, //Fallo al intentar borrar una carpeta predefinida

		};

		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Test numero: " + i);
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3], (String) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
		}
	}
	// Ancillary methods ------------------------------------------------------

	protected void template(final String username, final String folderName, final String parentFolder, final Class<?> expected, final String operation) {
		Class<?> caught;

		caught = null;
		try {
			Folder folder;
			Folder parent;

			folder = this.folderService.findOne(super.getEntityId(folderName));
			if (parentFolder != null)
				parent = this.folderService.findOne(super.getEntityId(parentFolder));
			else
				parent = null;

			super.authenticate(username);

			if (operation.equals("create"))
				if (null == null)
					this.folderService.createForUser(folderName);
				else
					this.folderService.createForUser(folderName, parent.getId());
			else if (operation.equals("delete"))
				this.folderService.delete(folder);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
