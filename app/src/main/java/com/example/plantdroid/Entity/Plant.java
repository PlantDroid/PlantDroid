package com.example.plantdroid.Entity;

public class Plant {
    int _plant_id;
    String _plant_name;
    String _plant_common_name;
    String _description;
    String _img;
    String _edible_parts;
    String _propagation_methods;
    Boolean _is_owned;

    String _kindom;
    String _phylum;
    String _class;
    String _order;
    String _family;
    String _genus;

    public Plant(int _plant_id, String _plant_name, String _plant_common_name, String _description, String _img, String _edible_parts, String _propagation_methods, Boolean _is_owned, String _kindom, String _phylum, String _class, String _order, String _family, String _genus) {
        this._plant_id = _plant_id;
        this._plant_name = _plant_name;
        this._plant_common_name = _plant_common_name;
        this._description = _description;
        this._img = _img;
        this._edible_parts = _edible_parts;
        this._propagation_methods = _propagation_methods;
        this._is_owned = _is_owned;
        this._kindom = _kindom;
        this._phylum = _phylum;
        this._class = _class;
        this._order = _order;
        this._family = _family;
        this._genus = _genus;
    }

    public int get_plant_id() {
        return _plant_id;
    }

    public void set_plant_id(int _plant_id) {
        this._plant_id = _plant_id;
    }

    public String get_plant_name() {
        return _plant_name;
    }

    public void set_plant_name(String _plant_name) {
        this._plant_name = _plant_name;
    }

    public String get_plant_common_name() {
        return _plant_common_name;
    }

    public void set_plant_common_name(String _plant_common_name) {
        this._plant_common_name = _plant_common_name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_img() {
        return _img;
    }

    public void set_img(String _img) {
        this._img = _img;
    }

    public String get_edible_parts() {
        return _edible_parts;
    }

    public void set_edible_parts(String _edible_parts) {
        this._edible_parts = _edible_parts;
    }

    public String get_propagation_methods() {
        return _propagation_methods;
    }

    public void set_propagation_methods(String _propagation_methods) {
        this._propagation_methods = _propagation_methods;
    }

    public Boolean get_is_owned() {
        return _is_owned;
    }

    public void set_is_owned(Boolean _is_owned) {
        this._is_owned = _is_owned;
    }

    public String get_kindom() {
        return _kindom;
    }

    public void set_kindom(String _kindom) {
        this._kindom = _kindom;
    }

    public String get_phylum() {
        return _phylum;
    }

    public void set_phylum(String _phylum) {
        this._phylum = _phylum;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String get_order() {
        return _order;
    }

    public void set_order(String _order) {
        this._order = _order;
    }

    public String get_family() {
        return _family;
    }

    public void set_family(String _family) {
        this._family = _family;
    }

    public String get_genus() {
        return _genus;
    }

    public void set_genus(String _genus) {
        this._genus = _genus;
    }
}
