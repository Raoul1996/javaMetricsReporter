package metrics;

import io.github.biezhi.ome.OhMyEmail;

import java.security.GeneralSecurityException;

// TODO: https://github.com/biezhi/oh-my-email
public class EmailSender{
    public void before() throws GeneralSecurityException{
        OhMyEmail.config(OhMyEmail.SMTP_QQ(false));
    }
}
