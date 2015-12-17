package com.desprogramar.jhipster.cucumber;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.desprogramar.jhipster.Application;
import com.desprogramar.jhipster.web.rest.UserResource;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class UserStepDefs {

//    @Inject
//    private UserResource userResource;

    private MockMvc restUserMockMvc;

	private ResultActions actions;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
//        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();

        // create security-aware mockMvc
        this.restUserMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

	@When("^I search user '(.*)'$")
	public void i_search_user_admin(String userId) throws Throwable {
        actions = restUserMockMvc.perform(get("/api/users/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .with(user("admin").roles("ADMIN")));
    }

	@Then("^the user is found$")
	public void the_user_is_found() throws Throwable {
		actions
	        .andExpect(status().isOk())
	        .andExpect(content().contentType("application/json"));
	}

	@Then("^his last name is '(.*)'$")
	public void his_last_name_is(String lastName) throws Throwable {
		actions.andExpect(jsonPath("$.lastName").value(lastName));
	}

}
