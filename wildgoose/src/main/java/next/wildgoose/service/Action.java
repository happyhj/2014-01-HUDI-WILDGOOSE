package next.wildgoose.service;

import next.wildgoose.utility.Uri;

public interface Action {
	public ActionResult execute(Uri uri);
}
