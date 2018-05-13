package controllers;

import objects.VewoteDatabase;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    public Result homeView() {
        return ok(views.html.home.render());
    }

    public Result browseView() {
        return ok(views.html.browse.render(VewoteDatabase.INSTANCE.getPolls()));
    }

}
