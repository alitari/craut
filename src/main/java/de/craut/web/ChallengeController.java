package de.craut.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.craut.domain.Challenge;
import de.craut.service.ChallengeService;

@Controller
@RequestMapping("/challenges")
@EnableAutoConfiguration
public class ChallengeController extends AbstractController {

	@Autowired
	protected ChallengeService challengeService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer pageNumber, Model model) {
		fillPageContent(model, pageNumber == null || pageNumber < 0 ? 0 : pageNumber);
		return "challenges";
	}

	private void fillPageContent(Model model, int pageNumber) {
		Page<Challenge> challengePage = challengeService.fetchChallenges(pageNumber);
		fillPageContent(model, "challenges", challengePage);
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "id", required = true) Long id, Model model) {
		challengeService.deleteChallenge(id);
		fillPageContent(model, 0);
		return "challenges";
	}

	@RequestMapping("/create")
	public String create(Model model) {
		fillPageContent(model, 0);
		return "challenge";
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = true) Long id, Model model) {
		fillPageContent(model, "Challenges");
		fillChallenge(model, id);
		return "challenge";
	}

	@RequestMapping("/edited")
	public String edited(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "name", required = true) String name, Model model) {
		Challenge challenge = challengeService.fetchChallenge(id);
		challenge.setName(name);
		challengeService.updateChallange(challenge);
		fillPageContent(model, 0);
		return "challenges";
	}

	private void fillChallenge(Model model, Long id) {
		Challenge challenge = challengeService.fetchChallenge(id);
		model.addAttribute("challenge", challenge);
	}
}