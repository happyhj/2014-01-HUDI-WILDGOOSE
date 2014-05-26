package next.wildgoose.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
	public void show(Object resultData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
