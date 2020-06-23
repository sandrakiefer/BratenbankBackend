package de.hsrm.mi.web.bratenbank.bratservice;

import java.util.List;
import java.util.Optional;
import javax.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hsrm.mi.web.bratenbank.benutzer.Benutzer;
import de.hsrm.mi.web.bratenbank.benutzer.BenutzerRepository;
import de.hsrm.mi.web.bratenbank.bratrepo.Braten;
import de.hsrm.mi.web.bratenbank.bratrepo.BratenRepository;

@Service
public class BratServiceImpl implements BratenService {

    @Autowired
    SimpMessagingTemplate broker;

    @Autowired
    BratenRepository bratenrepo;

    @Autowired
    BenutzerRepository benutzerrepo;

    @Override
    public List<Braten> alleBraten() {
        return bratenrepo.findAll();
    }

    @Override
    public Optional<Braten> sucheBratenMitId(int id) {
        return bratenrepo.findById(id);
    }

    @Transactional
    @Override
    public Braten editBraten(String loginname, Braten braten) {
        try {
            Benutzer b = benutzerrepo.findByLoginname(loginname);
            if (b == null) {
                throw new BratenServiceException();
            }
            braten.setAnbieter(b);
            b.getAngebote().add(braten);
            BratenMessage msg = new BratenMessage("change", braten);
            broker.convertAndSend("/topic/braten", msg);
            return bratenrepo.save(braten);
        } catch (OptimisticLockException e) {
            throw new BratenServiceException();
        }
    }

    @Override
    public void loescheBraten(int bratendatenid) {
        BratenMessage msg = new BratenMessage("delete", sucheBratenMitId(bratendatenid).get());
        broker.convertAndSend("/topic/braten", msg);
        bratenrepo.deleteById(bratendatenid);
    }
    
}
