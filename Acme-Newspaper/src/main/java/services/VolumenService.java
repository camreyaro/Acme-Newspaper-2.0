
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
		//El creador es el logeado
		Assert.isTrue(LoginService.getPrincipal().equals(v.getUser().getUserAccount()), "volumen.creator.error");
		Assert.isTrue(v.getId() == 0, "volumen.edit.error");

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

		final Volumen original = this.volumenRepository.findOne(volumen.getId());

		if (volumen.getId() == 0) {

			final LocalDate date = new LocalDate();

			//dfault properties
			volumen.setUser((User) this.actorService.findByPrincipal());
			volumen.setYear(date.getYear());
			volumen.setNewspapers(new ArrayList<Newspaper>());

		} else {

			volumen.setUser(original.getUser());
			volumen.setYear(original.getYear());
			volumen.setNewspapers(original.getNewspapers());
		}

		this.validator.validate(volumen, binding);

		return volumen;

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

	public Collection<Volumen> getMyAvailableVolumes(final Integer newsaperId) {
		return this.volumenRepository.getAvailaleVolumens(this.actorService.findByPrincipal().getId(), newsaperId);
	}

	//Dashboard

	public Double avgNewsPerVol() {
		return this.volumenRepository.avgOfNewspaperPerVolumen();
	}
	
	//Paginated repository
	public Page<Volumen> getMyNoSuscribedVolumensPaginate(final Integer pageNumber,
			final Integer pageSize) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.volumenRepository.getVolumensNotSuscribedByCustomerPaginate(this.actorService.findByPrincipal().getId(), request);
	}
	
	public Page<Volumen> findAllPaginate(final Integer pageNumber,
			final Integer pageSize) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.volumenRepository.findAllPaginate(request);
	}
	
	public Page<Volumen> getMyCreatedVolumensPaginate(final Integer pageNumber,
			final Integer pageSize) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.volumenRepository.getVolumensByUserPaginate(this.actorService.findByPrincipal().getId(), request);
	}
	
	public Page<Volumen> getMyVolumensPaginate(final Integer pageNumber,
			final Integer pageSize) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.volumenRepository.getVolumensByCustomerPaginate(this.actorService.findByPrincipal().getId(), request);
	}
	
	public Page<Newspaper> getAllNewspaperPaginate(final Integer pageNumber,
			final Integer pageSize, final Integer id) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.volumenRepository.getAllNewspaper(id, request);
	}
}
