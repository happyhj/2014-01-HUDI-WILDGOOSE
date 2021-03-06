package next.wildgoose.framework;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
	void show(HttpServletRequest request, HttpServletResponse response, Result resultData) throws ServletException, IOException;
}
