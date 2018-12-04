package com.voting.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Entity to map user info
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@Entity(tableName = "tbl_users")
public class UserEntity implements Parcelable {
    @NonNull
    private String forename;

    @NonNull
    private String surname;

    @NonNull @PrimaryKey
    private Long pesel;

    @NonNull
    public String getForename() {
        return forename;
    }

    public void setForename(@NonNull String forename) {
        this.forename = forename;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    public void setSurname(@NonNull String surname) {
        this.surname = surname;
    }

    @NonNull
    public Long getPesel() {
        return pesel;
    }

    public void setPesel(@NonNull Long pesel) {
        this.pesel = pesel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.forename);
        dest.writeString(this.surname);
        dest.writeValue(this.pesel);
    }

    public UserEntity() {}

    protected UserEntity(Parcel in) {
        this.forename = in.readString();
        this.surname = in.readString();
        this.pesel = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}
