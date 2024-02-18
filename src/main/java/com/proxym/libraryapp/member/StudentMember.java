package com.proxym.libraryapp.member;

public class StudentMember extends Member {

    private int year;

    public StudentMember(String name, int year) {
        super(name);
        this.year = year;
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



    @Override
    public void payBook(int numberOfDays) {
        float fee = 0.0f;
        int freeDays = (year == 1) ? 15 : 0;
        int chargeableDays = Math.max(numberOfDays - freeDays, 0);

        if (numberOfDays > 30) {

            fee += (chargeableDays - 30) * 0.10f;
        } else {

            fee += chargeableDays * 0.10f;
        }

        setWallet(getWallet() - fee);
    }

}
