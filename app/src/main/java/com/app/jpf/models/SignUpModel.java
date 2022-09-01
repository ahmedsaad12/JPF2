package com.app.jpf.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.app.jpf.BR;
import com.app.jpf.R;


public class SignUpModel extends BaseObservable {
    private int area_id;
    private int governate_id;
    private String first_name;
    private String seconed_name;
    private String third_name;
    private String address;
    private double lat;
    private double lng;
    private String national_num;


    public ObservableField<String> error_first_name = new ObservableField<>();
    public ObservableField<String> error_seconed_name = new ObservableField<>();
    public ObservableField<String> error_third_name = new ObservableField<>();

    public ObservableField<String> error_national = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!first_name.trim().isEmpty()
                &&
                !seconed_name.trim().isEmpty() &&
                !third_name.trim().isEmpty() &&
                !address.trim().isEmpty() &&
                !national_num.trim().isEmpty() &&
                governate_id != 0 && area_id != 0


        ) {
            error_first_name.set(null);
            error_seconed_name.set(null);
            error_third_name.set(null);
            error_address.set(null);
            error_national.set(null);
            return true;
        } else {

            if (first_name.trim().isEmpty()) {
                error_first_name.set(context.getString(R.string.field_required));

            } else {
                error_first_name.set(null);

            }
            if (seconed_name.trim().isEmpty()) {
                error_seconed_name.set(context.getString(R.string.field_required));

            } else {
                error_seconed_name.set(null);

            }
            if (third_name.trim().isEmpty()) {
                error_third_name.set(context.getString(R.string.field_required));

            } else {
                error_third_name.set(null);

            }
            if (address.trim().isEmpty()) {
                error_address.set(context.getString(R.string.field_required));

            } else {
                error_address.set(null);

            }
            if (national_num.trim().isEmpty()) {
                error_national.set(context.getString(R.string.inv_email));

            } else {
                error_national.set(null);

            }
            if (governate_id == 0) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_gove), Toast.LENGTH_LONG).show();
            }
            if (area_id == 0) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_area), Toast.LENGTH_LONG).show();
            }

            return false;
        }
    }

    public SignUpModel() {
        setThird_name("");
        setAddress("");
        setArea_id(1);
        setFirst_name("");
        setLat(0);
        setLng(0);
        setSeconed_name("");
        setGovernate_id(1);
        setNational_num("");

    }


    @Bindable
    public String getThird_name() {
        return third_name;
    }

    public void setThird_name(String third_name) {
        this.third_name = third_name;
        notifyPropertyChanged(BR.third_name);

    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getGovernate_id() {
        return governate_id;
    }

    public void setGovernate_id(int governate_id) {
        this.governate_id = governate_id;
    }
    @Bindable
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
        notifyPropertyChanged(BR.first_name);

    }
    @Bindable
    public String getSeconed_name() {
        return seconed_name;
    }

    public void setSeconed_name(String seconed_name) {
        this.seconed_name = seconed_name;
        notifyPropertyChanged(BR.seconed_name);

    }
    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    @Bindable
    public String getNational_num() {
        return national_num;
    }

    public void setNational_num(String national_num) {
        this.national_num = national_num;
        notifyPropertyChanged(BR.national_num);

    }
}
