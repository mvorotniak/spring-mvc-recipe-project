package com.mvoro.developer.springmvcrecipeproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mvoro.developer.springmvcrecipeproject.commands.RecipeCommand;
import com.mvoro.developer.springmvcrecipeproject.services.RecipeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/recipes")
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @RequestMapping("/{id}/show")
    public String showRecipe(@PathVariable Long id, Model model) {
        log.info("Showing recipe by id page...");
        model.addAttribute("recipe", recipeService.findById(id));

        return "recipe";
    }

    @GetMapping
    @RequestMapping("/new")
    public String createNewRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return "recipeform";
    }

    /**
     * Saves a recipe if there is no such recipe in the DB. If there is - updates the existing one.
     * @param recipeCommand model attribute populated with data from a form submitted to the /recipe endpoint
     * @return redirects to a page that shows the recently created recipe
     */
    @PostMapping
    @RequestMapping("/recipe")
    public String saveOrUpdateRecipe(@ModelAttribute RecipeCommand recipeCommand) {
        log.info("Creating new recipe with title '{}'...", recipeCommand.getDescription());
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        return "redirect:/recipes/" + savedRecipeCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("/{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        log.info("Updating recipe with id {}...", id);
        model.addAttribute("recipe", recipeService.findCommandById(id));

        return "recipeform";
    }

    @GetMapping
    @RequestMapping("{id}/delete")
    public String deleteRecipe(@PathVariable Long id) {
        log.info("Deleting recipe with id {}...", id);
        recipeService.deleteById(id);

        // Redirect to the main page
        return "redirect:/";
    }

}
