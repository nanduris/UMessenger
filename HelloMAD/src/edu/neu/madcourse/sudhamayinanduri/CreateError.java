/* The application crashes while trying to access this file. This happens because this activity has
 * been commented in the 'AndroidManifest.xml' file. 
 */
package edu.neu.madcourse.sudhamayinanduri;


import edu.neu.madcourse.sudhamayinanduri.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class CreateError extends Activity{

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_error);
	}
}
