package com.example.juan.concesionario;

public class ExtrasPresupuesto {
    private Extra extra;
    private boolean check;

    public ExtrasPresupuesto(Extra extra) {
        this.extra = extra;
        this.check = false;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public boolean isCheckbox() {
        return check;
    }

    public void setCheckbox(boolean check) {
        this.check = check;
    }
}
