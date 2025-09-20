package domina.springboot.verduleria.back;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin // con esta línea logro que funcione desde Angular
@RequestMapping("/verduras")
public class VerduleriaController {

	private final VerduleriaRepository verduleriaRepository;

	private VerduleriaController(VerduleriaRepository verduleriaRepository) {
		this.verduleriaRepository = verduleriaRepository;
	}

	@GetMapping("/{id}")
	private ResponseEntity<Verdura> findById(@PathVariable Long id) {
		Optional<Verdura> verduraOptional = verduleriaRepository.findById(id);
		if (verduraOptional.isPresent()) {
			return ResponseEntity.ok(verduraOptional.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping()
	private ResponseEntity<Verdura> create(@RequestBody Verdura nuevaVerdura, UriComponentsBuilder ucb) {
		Verdura verdura = verduleriaRepository.save(nuevaVerdura);
		URI uriVerdura = ucb.path("verduras/{id}").buildAndExpand(verdura.getId()).toUri();
		return ResponseEntity.status(HttpStatus.CREATED).location(uriVerdura).body(verdura);
	}

	@GetMapping
	private ResponseEntity<Page<Verdura>> findAll(Pageable pageable) {
		Page<Verdura> pag = verduleriaRepository.findAll(PageRequest.of(pageable.getPageNumber(),
				pageable.getPageSize(), pageable.getSortOr(Sort.by(Direction.ASC, "precio"))));
		return ResponseEntity.ok(pag);
	}

}
