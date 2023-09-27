package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.model.Otp;
import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.data.repositories.OtpRepository;
import com.financeTracker.financeTracker.exceptions.OtpException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    @Override
    public Otp generateOtp(AppUser appUser) throws OtpException {
        String otpGenerated = generateRandomDigit();
        Otp otpToSave = new Otp();
        otpToSave.setAppUser(appUser);
        otpToSave.setOtp(otpGenerated);
        return otpRepository.save(otpToSave);
    }


    private  String generateRandomDigit() throws OtpException {
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        String newOtp =  String.format("%04d", number);
        if(newOtp.isEmpty() || newOtp.trim().isEmpty()){
            generateRandomDigit();
        }

        if(checkIfOtpExist(newOtp)){
            generateRandomDigit();
        }
        return newOtp;
    }

    private  boolean checkIfOtpExist(String otp) throws OtpException {
        if(otp.isEmpty() || otp.trim().isEmpty()){
            throw new OtpException("Otp not generated");
        }
        Optional<Otp> otpFound = otpRepository.findOtpByOtp(otp);
        return otpFound.isPresent();
    }

    @Override
    public boolean verifyOtp(String otp){
        try{
            if(otp.isBlank() || otp.isEmpty()){
                throw new OtpException("Otp cannot be empty");
            }

            boolean verified = checkIfOtpExist(otp);
            if(!verified){
                throw new OtpException("Could not verify otp");
            }
            return true;
        }catch (OtpException e){
            return false;

        }
    }

    @Override
    public Otp findOtpByOtp(String otp) throws OtpException {
        Optional<Otp> foundOtp =  otpRepository.findOtpByOtp(otp);
        if (foundOtp.isEmpty()){
            throw new OtpException("Otp not found");
        }
        return foundOtp.get();
    }
    @Override
    public void deleteOtp(Otp otp){
        otpRepository.delete(otp);
    }
}
