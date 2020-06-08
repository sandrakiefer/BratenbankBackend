package de.hsrm.mi.web.bratenbank.bratapi;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.bratenbank.bratrepo.Braten;
import de.hsrm.mi.web.bratenbank.bratservice.BratenService;
import de.hsrm.mi.web.bratenbank.bratservice.BratenServiceException;

@RestController
@RequestMapping("/api")
public class BratenRestApi {
    
    @Autowired
    BratenService bratenService;

    @GetMapping("/braten")
    public List<Braten> getAlleBraten() {
        return bratenService.alleBraten();
    }

    @GetMapping("/braten/{id}")
    public Braten getBraten(@PathVariable int id) throws BratenApiException {
        if (bratenService.sucheBratenMitId(id).isPresent()) {
            return bratenService.sucheBratenMitId(id).get();
        } else {
            throw new BratenApiException();
        }
    }

    @DeleteMapping("/braten/{id}")
    public void deleteBraten(@PathVariable int id) throws BratenApiException {
        if (bratenService.sucheBratenMitId(id).isPresent()) {
            bratenService.loescheBraten(id);
        } else {
            throw new BratenApiException();
        }
    }

    @PostMapping("/braten")
    public Braten postBraten(@RequestParam String loginname, @Valid @RequestBody Braten braten) throws BratenApiException {
        try {
            return bratenService.editBraten(loginname, braten);
        } catch (BratenServiceException e) {
            throw new BratenApiException();
        }
    }

}
