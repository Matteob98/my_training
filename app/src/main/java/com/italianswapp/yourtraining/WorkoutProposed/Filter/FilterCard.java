package com.italianswapp.yourtraining.WorkoutProposed.Filter;

/**
 * Classe per utilizzare le card "filtro" per gli allenamenti proposti
 * Usata sia dalla recyclerView del dialog per la selezione dei filtri sia dalla recyclerView della schermata workoutProposed
 */
class FilterCard {

    /**
     * Tipi di filtro
     */
    public enum Type {
        MUSCLE,
        LEVEL
    };

    private String name;
    private Type type;

    public FilterCard(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public FilterCard () { this("", Type.MUSCLE); }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
