
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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


	public Volumen create() {
		final Volumen res = new Volumen();

		res.setUser((User) this.actorService.findByPrincipal());

		return res;

	}

	public Volumen findOne(final Integer id) {
		return this.volumenRepository.findOne(id);
	}

	public void delete(final Volumen v) {
		Assert.notNull(v.getId());
		Assert.isTrue(v.getId() > 0);

		this.volumenRepository.delete(v.getId());
	}

	public Volumen save(final Volumen v) {
		//El editor tiene que ser el que lo cre�
		Assert.isTrue(LoginService.getPrincipal().equals(v.getUser().getUserAccount()), "volumen.creator.error");

		return this.volumenRepository.save(v);

	}

	public void addNewspaper(final Volumen v, final Newspaper p) {

		final Collection<Newspaper> newspapers = v.getNewspapers();

		Assert.isTrue(p.getPublished(), "volumen.newspaper.published.error"); //No puedes a�adir un periodico no publicado
		Assert.isTrue(LoginService.getPrincipal().equals(v.getUser().getUserAccount()), "volumen.creator.error"); //No puedes a�adir periodicos a un volumen que no has creado tu
		Assert.isTrue(!newspapers.contains(p), "volumen.contained.newspaper.error"); //No puedes a�adir periodicos que ya estan a�adidos

		newspapers.add(p);
		v.setNewspapers(newspapers);

		this.volumenRepository.save(v);

	}

	public void removeNewspaper(final Volumen v, final Newspaper p) {

		final Collection<Newspaper> newspapers = v.getNewspapers();

		Assert.isTrue(LoginService.getPrincipal().equals(v.getUser().getUserAccount()), "volumen.creator.error"); //No puedes eliminar periodicos de un volumen que no has creado tu
		Assert.isTrue(newspapers.remove(p), "volumen.uncontained.newspaper.error"); //No puedes eliminar periodicos que no estan a�adidos

		v.setNewspapers(newspapers);

		this.volumenRepository.save(v);

	}
}
