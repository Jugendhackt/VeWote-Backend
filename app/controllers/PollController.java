package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class PollController extends Controller {

    public Result createView() {
        return ok(views.html.create.render());
    }

}
