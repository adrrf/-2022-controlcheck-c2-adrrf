package org.springframework.samples.petclinic.vet;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class VetFormatter implements Formatter<Vet>{

    @Autowired
    VetService vs;


    @Override
    public String print(Vet object, Locale locale) {
        return String.valueOf(object.getId());
    }

    @Override
    public Vet parse(String text, Locale locale) throws ParseException {
        Integer id=Integer.parseInt(text);
        Vet v=vs.getById(id);
        if(v==null)
            throw new ParseException("Vet with id "+text+" not found.", 0);
        return v;
    }
    
}
