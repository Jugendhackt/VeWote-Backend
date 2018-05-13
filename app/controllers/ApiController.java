package controllers;

import objects.Poll;
import objects.VewoteDatabase;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class ApiController extends Controller {

    @Inject
    FormFactory factory;

    public Result create() {
        Form<Poll> form = factory.form(Poll.class).bindFromRequest();
        Poll poll = form.get();

        VewoteDatabase.INSTANCE.storePoll(poll);
        return redirect("/browse");
    }

}
