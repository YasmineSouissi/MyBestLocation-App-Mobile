package com.example.mybestlocation;

public class Position {
    int idposition;
    Double longitude ;
    Double latitude;
    String pseudo;
    int idUser;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Position(Double latitude, Double longitude, String pseudo, int idUser) {
        this.idUser = idUser;
        this.pseudo = pseudo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Position(Double longitude, Double latitude, String pseudo) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.pseudo = pseudo;
    }

    public Position(int idposition, Double longitude, Double latitude, String pseudo) {
        this.idposition = idposition;
        this.longitude = longitude;
        this.latitude = latitude;
        this.pseudo = pseudo;
    }

    @Override
    public String toString() {
        return "Position{" +
                "idposition=" + idposition +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", pseudo='" + pseudo + '\'' +
                '}';
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdposition() {
        return idposition;
    }

    public void setIdposition(int idposition) {
        this.idposition = idposition;
    }


    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}

