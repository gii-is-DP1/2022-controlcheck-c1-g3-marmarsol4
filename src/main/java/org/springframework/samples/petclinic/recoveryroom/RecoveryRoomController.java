package org.springframework.samples.petclinic.recoveryroom;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recoveryroom")
public class RecoveryRoomController {

	private static final String VIEWS_ROOMS_CREATE_OR_UPDATE_FORM = "recoveryroom/createOrUpdateRecoveryRoomForm";
	
    private final RecoveryRoomService recoveryRoomService;

    @Autowired
    public RecoveryRoomController(RecoveryRoomService recoveryRoomService) {
		this.recoveryRoomService = recoveryRoomService;
	}
    
    @ModelAttribute("types")
    public List<RecoveryRoomType> populateRoomTypes() {
    	return recoveryRoomService.getAllRecoveryRoomTypes();
    	
    }
    
    @GetMapping(value = "/create")
	public String initCreationForm(ModelMap model) {
		RecoveryRoom rr = new RecoveryRoom();
        model.put("recoveryRoom", rr);
		return VIEWS_ROOMS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/create")
	public String processCreationForm(@Valid RecoveryRoom rr, BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			return VIEWS_ROOMS_CREATE_OR_UPDATE_FORM;
		}
		else {
            try {
				this.recoveryRoomService.save(rr);
			} catch (DuplicatedRoomNameException e) { 
				result.rejectValue("RecoveryRoom", "error", "The name of the room is duplicated for this type");
			}
            return "welcome";
		}
	}
	
}
