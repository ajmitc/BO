package bo.game.conspirator;

import bo.Model;
import bo.game.conspirator.ConspiratorCardEffect;
import bo.view.View;

public class ConspiratorEffectResolver {
    private Model model;
    private View view;

    public ConspiratorEffectResolver(Model model, View view){
        this.model = model;
        this.view = view;
    }

    public void resolveEffect(ConspiratorCardEffect effect){
        switch (effect){
            case DEFECTIONS_AND_DISSENT:
                // Handled in-line in Controller::conspire action
                break;
            case FALSE_ACCUSATIONS:
                break;
            case BLACK_ORCHESTRA:
                break;
            case PLANNING_SESSION:
                break;
            case QUICK_REACTION:
                break;
            case EFFECTIVE_PLANNING:
                break;
            case DUMPSTER_DIVE:
                break;
            case DELAY_HITLER:
                break;
            case SLOW_NEWS_DAY:
                break;
            case SAFE:
                break;
            case AIRPLANE_ACCESS:
                break;
            case HITLERS_SCHEDULE_LEAKED:
                break;
            case INFILTRATE_BUNKER:
                break;
            case BLACK_MARKET:
                break;
            case COVER_STORY:
                break;
            case HISTORY_REPEATS:
                break;
            case CALLED_AWAY:
                break;
            case DISTANT_CONTACT:
                break;
            case LOSE_GESTAPO:
                break;
            case LONE_REVOLVER:
                break;
            case SUITCASE_BOMB:
                break;
            case DERAIL_TRAIN:
                break;
            case PLANE_BOMB:
                break;
            case HIDDEN_BOMB:
                break;
            case COUP:
                break;
            case POISON_GAS:
                break;
            case SNIPER:
                break;
            case KIDNAPPING:
                break;
            case POISON_PARCEL:
                break;
            case POISON_FOOD:
                break;
            case AMBUSH:
                break;
            case PARTISAN_INFORMANT:
                break;
            case CLASSIFIED_PAPERS:
                break;
            case WAR_CRIMES_EVIDENCE:
                break;
            case FORGED_DOCUMENTS:
                break;
            case MILITARY_SECRETS:
                break;
            case INTELLIGENCE_BRIEFING:
                break;
            case HIDDEN_CAMERA:
                break;
            case FALSE_SAFETY_REPORT:
                break;
            case INSPIRING_LETTER:
                break;
            case OFFICER_RECRUITED:
                break;
            case CONCEALED_PISTOL:
                break;
            case FORGED_RELEASE_ORDERS:
                break;
            default:
                break;
        }
    }

    private void handleFalseAccusations(){

    }

    private void handleBlackOrchestra(){}
    private void handlePlanningSession(){}
    private void handleQuickReaction(){}
    private void handleEffectivePlanning(){}
    private void handleDUMPSTER_DIVE(){}
    private void handleDELAY_HITLER(){}
    private void handleSLOW_NEWS_DAY(){}
    private void handleSAFE(){}
    private void handleAIRPLANE_ACCESS(){}
    private void handleHITLERS_SCHEDULE_LEAKED(){}
    private void handleINFILTRATE_BUNKER(){}
    private void handleBLACK_MARKET(){}
    private void handleCOVER_STORY(){}
    private void handleHISTORY_REPEATS(){}
    private void handleCALLED_AWAY(){}
    private void handleDISTANT_CONTACT(){}
    private void handleLOSE_GESTAPO(){}
    private void handleLONE_REVOLVER(){}
    private void handleSUITCASE_BOMB(){}
    private void handleDERAIL_TRAIN(){}
    private void handlePLANE_BOMB(){}
    private void handleHIDDEN_BOMB(){}
    private void handleCOUP(){}
    private void handlePOISON_GAS(){}
    private void handleSNIPER(){}
    private void handleKIDNAPPING(){}
    private void handlePOISON_PARCEL(){}
    private void handlePOISON_FOOD(){}
    private void handleAMBUSH(){}
    private void handlePARTISAN_INFORMANT(){}
    private void handleCLASSIFIED_PAPERS(){}
    private void handleWAR_CRIMES_EVIDENCE(){}
    private void handleFORGED_DOCUMENTS(){}
    private void handleMILITARY_SECRETS(){}
    private void handleINTELLIGENCE_BRIEFING(){}
    private void handleHIDDEN_CAMERA(){}
    private void handleFALSE_SAFETY_REPORT(){}
    private void handleINSPIRING_LETTER(){}
    private void handleOFFICER_RECRUITED(){}
    private void handleCONCEALED_PISTOL(){}
    private void handleFORGED_RELEASE_ORDERS(){}
}