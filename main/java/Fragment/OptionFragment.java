package Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.hungrytalk.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class OptionFragment extends Fragment {
    private View view;
    private GoogleMap googleMap;
    private FragmentActivity myContext;

   public OptionFragment(){

    }
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){super.onCreate(savedInstanceState);{
        return inflater.inflate(R.layout.option,container,false);

    }




}}





