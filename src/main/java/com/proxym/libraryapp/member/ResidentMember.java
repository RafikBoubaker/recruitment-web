package com.proxym.libraryapp.member;

public class ResidentMember extends Member {
    public ResidentMember(String name) {
        super(name);
    }


    @Override
    public void payBook(int numberOfDays) {
        int feeInCents = 0;
        if (numberOfDays <= 60) {
            feeInCents = numberOfDays * 10;
        } else {

            feeInCents += 60 * 10;

            feeInCents += (numberOfDays - 60) * 20;
        }

        float fee = feeInCents / 100.0f;
        setWallet(getWallet() - fee);
    }
}

