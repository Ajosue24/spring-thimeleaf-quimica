package com.proasecal.software.controlexterno.entity.documentacionjson;

import lombok.Getter;
import lombok.Setter;

public class Histograma {
    @Getter @Setter
    String category_selected="";

    @Getter
    String categories;
    Integer count;
    Integer min;
    Integer max;

    @Getter
    Double value;

    public Histograma(String categories, Integer min, Integer max) {
        this.categories = categories;
        this.min = min;
        this.max = max;
        this.count = 0;
        this.value = 0.0;
    }

    public boolean apply(Double value) {
        return value > min && value < max;
    }

    public void plus() {
        this.count++;
    }

    public void select() {
        this.category_selected = this.categories;
    }

    public void calculateValue(Integer total){
        this.value = Double.valueOf(Math.ceil((100*this.count) / total));
    }
}
