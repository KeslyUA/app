package com.idat.rentaflorv1.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.idat.rentaflorv1.Adapter.SolicitudAdapter;
import com.idat.rentaflorv1.MainActivity;
import com.idat.rentaflorv1.Modelo.Solicitudes;
import com.idat.rentaflorv1.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicioFinalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicioFinalFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static ArrayList<Solicitudes> listaSolicitud;
    ProgressDialog progress;

    //para poder consumir los servicios web
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest = null;

    //los componentes de mi xml
    RecyclerView recyclerservicio;
    public ServicioFinalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServicioFinalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServicioFinalFragment newInstance(String param1, String param2) {
        ServicioFinalFragment fragment = new ServicioFinalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =inflater.inflate(R.layout.fragment_servicio_final, container, false);
        //recepciono los componentes de mi xml
        recyclerservicio = view.findViewById(R.id.recyclerserviciofin);
        //inicializo mis variables creadas
        listaSolicitud = new ArrayList<>();
        //inicializo variable para consumir web servico con Volley
        request = Volley.newRequestQueue(getContext());
        //formato del recyclerview
        recyclerservicio.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerservicio.setHasFixedSize(true);

        cargarWSlistaServicio();

        return view;
    }

    private void cargarWSlistaServicio() {

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando ...");
        progress.show();
        String id = MainActivity.idUsuario;
        String url = "http://192.168.100.10/rentaflor/listar_servicio.php?estado=5&&id="+id;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progress.hide();
       // Toast.makeText(getContext(), R.string.sms_error_de_coneccion, Toast.LENGTH_LONG).show();
        System.out.println();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progress.hide();
        Solicitudes solicitudes = null;
        JSONArray jsonArray = response.optJSONArray("servicio");

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                solicitudes = new Solicitudes();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                solicitudes.setIdcotizacion(jsonObject.optInt("ID_COTIZACION"));
                solicitudes.setNumsolicitud(jsonObject.optString("NUMERO_SOLICITUD"));
                solicitudes.setFechainicio(jsonObject.optString("FECHA_INICIO"));
                solicitudes.setFechafin(jsonObject.optString("FECHA_FIN"));
                solicitudes.setLigarinicio(jsonObject.optString("LUGAR_INICIO"));
                solicitudes.setLugarfin(jsonObject.optString("LUGAR_DESTINO"));
                solicitudes.setMotivo(jsonObject.optString("MOTIVO"));
                solicitudes.setEstado(jsonObject.optString("ESTADO"));
                solicitudes.setIdtiposervicio(jsonObject.optInt("ID_T_SERVICIO"));
                solicitudes.setDescripciontiposervicio(jsonObject.optString("DESCRIPCION_T_SERVICIO"));
                solicitudes.setIdcamioneta(jsonObject.optInt("ID_CAMIONETA"));
                solicitudes.setDescripcioncamioneta(jsonObject.optString("DESCRIPCION_CAMIONETA"));
                solicitudes.setCosto(jsonObject.optString("COSTO"));
                solicitudes.setClientenombre(jsonObject.optString("CLIENTENOMBRE"));
                solicitudes.setEmpresanombre(jsonObject.optString("EMPRESANOMBRE"));
                solicitudes.setRuc(jsonObject.optString("RUC"));
                solicitudes.setClienteservicio(jsonObject.optString("CLIENTESERVICIO"));
                solicitudes.setNumero(jsonObject.optString("NUMERO"));
                solicitudes.setCargoservicio(jsonObject.optString("CARGOSERVICIO"));
                solicitudes.setReferencia(jsonObject.optString("REFERENCIA"));
                solicitudes.setFecha(jsonObject.optString("FECHA"));

                listaSolicitud.add(solicitudes);
            }

            SolicitudAdapter solicitudAdapter = new SolicitudAdapter(getContext());
            solicitudAdapter.AgregarElementos(listaSolicitud);
            recyclerservicio.setAdapter(solicitudAdapter);


        } catch (Exception e) {
            e.fillInStackTrace();
            // Toast.makeText(getContext(), R.string.sms_error_de_coneccion, Toast.LENGTH_SHORT).show();
            Log.e("Error", e.toString());
            progress.hide();
        }

    }
}