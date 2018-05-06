
package services;

import java.util.Calendar;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Advertisement;
import domain.CreditCard;
import domain.Newspaper;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdvertisementServiceTest extends AbstractTest {

	// The SUT -------------------------------------

	@Autowired
	private AdvertisementService	advertisementService;
	@Autowired
	private NewspaperService		newspaperService;
	private Object					oops;


	@Test
	public void sampleDriver() {
		final Object testingData[][] = {

			{
				"agent1", "newspaper3", null, "create", "http://advertisement.com", null
			}, //Crear un advertisement
			{
				"agent1", "newspaper3", ConstraintViolationException.class, "create", "no es url", null
			}, //No puedes crear un advertisement, con url invalida.
			{
				"agent1", "newspaper3", null, "save", "http://advertisement.com", "advertisement1"
			}, //Puedes editar un advertisement
			{
				"admin", "newspaper3", IllegalArgumentException.class, "save", null, "advertisement1"
			}, //No puedes editar un advertisement que no es tuyo
			{
				"agent1", "newspaper3", IllegalArgumentException.class, "saveCC", null, "advertisement1"
			}, //No puedes editar un advertisement con ccard invalida

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String username, final String newspaperTitle, final Class<?> expected, final String operation, final String targetURl, final String advertisement1) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			if (operation.equals("create")) {
				Newspaper newspaper = this.newspaperService.findOne(this.getEntityId(newspaperTitle));

				final Advertisement advertisement = this.advertisementService.create(newspaper);
				final CreditCard creditCard = new CreditCard();
				creditCard.setBrandName("BrandName");
				creditCard.setCvvCode(789);
				creditCard.setExpirationMonth(12);
				creditCard.setExpirationYear(2020);
				creditCard.setHolderName("Holder Name");
				creditCard.setNumber("1234567891234567");

				advertisement.setCreditCard(creditCard);
				advertisement.setPrice(20.0);
				advertisement.setTitle("Title");
				advertisement.setUrlBanner("https://image.freepik.com/iconos-gratis/jpg-variante-de-formato-de-archivo_318-45505.jpg");
				advertisement.setUrlTargetPage(targetURl);
				this.advertisementService.saveAndFlush(advertisement);
			} else if (operation.equals("save")) {
				final Advertisement advertisement = this.advertisementService.findOne(this.getEntityId(advertisement1));
				advertisement.setUrlTargetPage(targetURl);
				this.advertisementService.save(advertisement);
			} else if (operation.equals("saveCC")) {
				final Advertisement advertisement = this.advertisementService.findOne(this.getEntityId(advertisement1));
				final CreditCard creditCard = new CreditCard();
				creditCard.setBrandName("BrandName");
				creditCard.setCvvCode(789);
				creditCard.setExpirationMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
				creditCard.setExpirationYear(Calendar.YEAR);
				creditCard.setHolderName("Holder Name");
				creditCard.setNumber("1234567891234567");
				advertisement.setCreditCard(creditCard);
				this.advertisementService.save(advertisement);
			}

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
