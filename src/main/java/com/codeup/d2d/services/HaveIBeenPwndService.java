package com.codeup.d2d.services;

import me.legrange.haveibeenpwned.HaveIBeenPwndApi;
import me.legrange.haveibeenpwned.HaveIBeenPwndException;
import org.springframework.stereotype.Service;

@Service("pwndService")
public class HaveIBeenPwndService {

    public HaveIBeenPwndApi hibp;

    public boolean CheckPasswordForPwnage(String password) {
        try {
            hibp = new HaveIBeenPwndApi("My-Pwnage-Testing-App");
            boolean pwned = hibp.isPlainPasswordPwned(password);
            System.out.printf("That silly password %s pwned!\n", (pwned ? "is" : "isn't"));
            return pwned;
        }catch(HaveIBeenPwndException e){
            return false;
        }
    }
}
