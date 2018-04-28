
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.VolumenRepository;
import security.LoginService;
import domain.Newspaper;
import domain.User;
import domain.Volumen;

@Service
@Transactional
public class VolumenService {

	@Autowired
	private VolumenRepository	volumenRepository;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private Validator			validator;


	public Volumen create() {
		final Volumen res = new Volumen();
		final LocalDate date = new LocalDate();

		res.setUser((User) this.actorService.findByPrincipal());
		res.setYear(date.getYear());
		res.setNewspapers(new ArrayList<Newspaper>());

		return res;

	}
	public Volumen findOne(final Integer id) {
		return this.volumenRepository.findOne(id);
	}

	public Collection<Volumen> findAll() {
		return this.volumenRepository.findAll();
	}

	public void delete(final Volumen v) {
		Assert.notNull(v.getId());
		Assert.isTrue(v.getId() > 0);

		this.volumenRepository.delete(v.getId());
	}

	public Volumen save(final Volumen v) {
		//El editor tiene que ser el que lo creó
		Assert.isTrue(LoginService.getPrincipal().equals(v.getUser().getUserAccount()), "volumen.creator.error");

		return this.volumenRepository.save(v);

	}

	public void addNewspaper(final Volumen v, final Newspaper p) {

		final Collection<Newspaper> newspapers = v.getNewspapers();

		Assert.isTrue(p.getPublished(), "volumen.newspaper.published.error"); //No puedes añadir un periodico no publicado
		Assert.isTrue(LoginService.getPrincipal().equals(v.getUser().getUserAccount()), "volumen.creator.error"); //No puedes añadir periodicos a un volumen que no has creado tu
		Assert.isTrue(!newspapers.contains(p), "volumen.contained.newspaper.error"); //No puedes añadir periodicos que ya estan añadidos

		newspapers.add(p);
		v.setNewspapers(newspapers);

		this.volumenRepository.save(v);

	}

	public void removeNewspaper(final Volumen v, final Newspaper p) {

		final Collection<Newspaper> newspapers = v.getNewspapers();

		Assert.isTrue(LoginService.getPrincipal().equals(v.getUser().getUserAccount()) || LoginService.getPrincipal().isAuthority("ADMIN"), "volumen.creator.error"); //No puedes eliminar periodicos de un volumen que no has creado tu
		Assert.isTrue(newspapers.remove(p), "volumen.uncontained.newspaper.error"); //No puedes eliminar periodicos que no estan añadidos

		v.setNewspapers(newspapers);

		this.volumenRepository.save(v);

	}

	public Volumen reconstruct(final Volumen volumen, final BindingResult binding) {
		final Volumen res;
		final Volumen original = this.volumenRepository.findOne(volumen.getId());

		if (volumen.getId() == 0) {
			res = new Volumen();
			final LocalDate date = new LocalDate();

			//dfault properties
			res.setUser((User) this.actorService.findByPrincipal());
			res.setYear(date.getYear());
			res.setNewspapers(new ArrayList<Newspaper>());

			//editable properties
			res.setDescription(volumen.getDescription());
			res.setTitle(volumen.getTitle());

		} else {
			res = volumen;

			res.setUser(original.getUser());
			res.setYear(original.getYear());
			res.setNewspapers(original.getNewspapers());
		}

		this.validator.validate(res, binding);

		return res;

	}

	//Other repository methods

	public Collection<Newspaper> getPublicNewspaper(final Integer id) {
		return this.volumenRepository.getPublicNewspaper(id);
	}

	public Collection<Newspaper> getPrivateNewspaper(final Integer id) {
		return this.volumenRepository.getPrivateNewspaper(id);
	}

	public Collection<Newspaper> getAllNewspaper(final Integer id) {
		return this.volumenRepository.getAllNewspaper(id);
	}

	public Collection<Volumen> getVolumensOfNewspaper(final Integer id) {
		return this.volumenRepository.getVolumensOfNewspaper(id);
	}

	public Collection<Volumen> getMyVolumens() {
		return this.volumenRepository.getVolumensByCustomer(this.actorService.findByPrincipal().getId());
	}

	public Collection<Volumen> getMyCreatedVolumens() {
		return this.volumenRepository.getVolumensByUser(this.actorService.findByPrincipal().getId());
	}

	public Collection<Volumen> getMyNoSuscribedVolumens() {
		return this.volumenRepository.getVolumensNotSuscribedByCustomer(this.actorService.findByPrincipal().getId());
	}

	//Dashboard

	public Double avgNewsPerVol() {
		return this.volumenRepository.avgOfNewspaperPerVolumen();
	}
}
