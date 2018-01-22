package com.kdoctor.dialogs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.models.VaccineCenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class HospitalMapDialog extends DialogFragment implements OnMapReadyCallback {

    public final static int HANOI_CODE = 1;
    public final static int HOCHIMINH_CODE = 2;
    int type = 1;

    double lat = 10.7905063;
    double lon = 106.6823462;

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    GoogleMap googleMap;
    List<VaccineCenter> centers = new ArrayList<>();

    LocationListener locationListener;

    @BindView(R.id.map)
    MapView mapView;

    @SuppressLint("ValidFragment")
    public HospitalMapDialog(int provinceCode) {
        dialog = this;
        type = provinceCode;
        switch (provinceCode) {
            case HANOI_CODE:
                lat = 21.0228161;
                lon = 105.8018581;
                break;
            case HOCHIMINH_CODE:
                lat = 10.7905063;
                lon = 106.6823462;
                break;
            default:
                lat = 10.7905063;
                lon = 106.6823462;
                break;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                final double latitude = location.getLatitude();
                final double longitude = location.getLongitude();

                RestServices.getInstance().getServices().getNearestHospital(latitude, longitude, new Callback<VaccineCenter>() {
                    @Override
                    public void success(VaccineCenter vaccineCenter, Response response) {
                        markNearest(vaccineCenter);

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(vaccineCenter.getLat(), vaccineCenter.getLon()), 12);
                        if (!isMoved) {
                            googleMap.moveCamera(cameraUpdate);
                            isMoved = true;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    void markNearest(VaccineCenter vaccineCenter){
        for (Marker marker : markers
                ) {
            if (vaccineCenter != null) {
                if (marker.getTag().toString().equals(Integer.toString(vaccineCenter.getId()))) {
                    try {
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_nearest_hospital", dpToPx(32), dpToPx(32))));
                    }
                    catch (Exception e){
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_nearest_hospital));
                    }
                    marker.setZIndex(10);
                }
                else {
                    try {
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_hospital", dpToPx(32), dpToPx(32))));
                    }
                    catch (Exception e){
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_hospital));
                    }
                }
            }
        }
    }

    private static HospitalMapDialog dialog;

    public static HospitalMapDialog getInstance() {
        return dialog;
    }

    public List<VaccineCenter> provinceFilter(List<VaccineCenter> centers) {
        List<VaccineCenter> centerList = centers;
        /*
        List<VaccineCenter> centerList = new ArrayList<>();
        for (VaccineCenter center : centers
                ) {
            String address = center.getAddress().toLowerCase();
            switch (type) {
                case HANOI_CODE:
                    if (address.contains("hn") || address.contains("hà nội") || address.contains("ha noi")) {
                        centerList.add(center);
                    }
                    break;
                case HOCHIMINH_CODE:
                    if (address.contains("hcm") || address.contains("hồ chí minh") || address.contains("ho chi minh")) {
                        centerList.add(center);
                    }
                    break;
                default:
                    if (address.contains("hcm") || address.contains("hồ chí minh") || address.contains("ho chi minh")) {
                        centerList.add(center);
                    }
                    break;
            }
        }
        */
        return centerList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_vaccine_map, null);
        ButterKnife.bind(this, rootView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        builder.setTitle("Bản đồ");
        builder.setView(rootView);

        builder.setNeutralButton("Kết thúc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    String content = "";

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (final VaccineCenter center : centers
                        ) {
                    if (center.getId() == Integer.parseInt(marker.getTag().toString())) {
                        content = "";
                        if (center.getName() == null || center.getName().equals("")) {
                            center.setName("Chưa cập nhật.");
                        }

                        if (center.getAddress() == null || center.getAddress().equals("")) {
                            center.setAddress("Chưa cập nhật.");
                        }

                        if (center.getPhone() == null || center.getPhone().equals("")) {
                            center.setPhone("Chưa cập nhật.");
                        }

                        String ss = "";
                        if (!(center.getNote() == null || center.getNote().equals(""))) {
                            ss += "Ghi chú: " + center.getNote();
                        }
                        else{
                            center.setNote("Chưa cập nhật.");
                        }

                        if (center.getCalendar() == null || center.getCalendar().equals("")) {
                            center.setCalendar("Chưa cập nhật.");
                        }

                        content += "<b>" + center.getName() + "</b><br/>";
                        content += "Địa chỉ: " + center.getAddress() + "<br/>";
                        content += "Điện thoại liên hệ: " + center.getPhone() + "<br/>";
                        content += "Lịch làm việc: " + center.getCalendar() + "<br/>";
                        content += ss;

                        QuestionDialog dialog = new QuestionDialog("Thông tin", Html.fromHtml(content).toString(), new QuestionDialog.OnOneChoiceSelection() {
                            @Override
                            public void onButtonClick() {

                            }
                        });
                        dialog.show(getFragmentManager(), "");
                        break;
                    }
                }
                return false;
            }
        });

        addMarkers();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12);
        googleMap.moveCamera(cameraUpdate);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            try {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } catch (Exception e) {

            }
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    boolean isMoved = false;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1001;

    List<Marker> markers = new ArrayList<>();

    public void addMarkers() {
        RestServices.getInstance().getServices().getHospital(100.0, lat, lon, new Callback<List<VaccineCenter>>() {
            @Override
            public void success(List<VaccineCenter> vaccineCenters, Response response) {
                List<VaccineCenter> filterdCenters = provinceFilter(vaccineCenters);
                for (VaccineCenter center : filterdCenters
                        ) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(center.getLat(), center.getLon());
                    markerOptions.position(latLng);
                    try {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_hospital", dpToPx(32), dpToPx(32))));
                    }
                    catch (Exception e){
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_hospital));
                    }
                    Marker marker = googleMap.addMarker(markerOptions);
                    marker.setTag(center.getId());
                    markers.add(marker);
                }
                centers = filterdCenters;
                try {
                    findNearestCenter();
                }
                catch (Exception e){

                }
            }

            @Override
            public void failure(RetrofitError error) {
                showLog(error.getMessage());
            }
        });
    }

    LocationManager locationManager;

    public void findNearestCenter(){
        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, false), 1000, 0, locationListener);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else {
            try {
                Location location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));
                final double latitude = location.getLatitude();
                final double longitude = location.getLongitude();

                RestServices.getInstance().getServices().getNearestHospital(latitude, longitude, new Callback<VaccineCenter>() {
                    @Override
                    public void success(VaccineCenter vaccineCenter, Response response) {
                        markNearest(vaccineCenter);

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(vaccineCenter.getLat(), vaccineCenter.getLon()), 12);
                        if (!isMoved) {
                            googleMap.moveCamera(cameraUpdate);
                            isMoved = true;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
            catch (Exception e){
                Toast.makeText(getContext(), "Không thể xác định vị trị hiện tại, xin kiểm tra tín hiệu định vị.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*
    public void findNearestCenter(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else {//huy
            try {
                LocationManager locationManager = (LocationManager)
                        getActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, locationListener);
            }
            catch (Exception e){
                Toast.makeText(getContext(), "Không thể xác định vị trí hiện tại, xin kiểm tra tín hiệu định vị.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    */
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void showLog(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();

        try {
            locationManager.removeUpdates(locationListener);
        }
        catch (Exception e){

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialog = null;
    }
}
