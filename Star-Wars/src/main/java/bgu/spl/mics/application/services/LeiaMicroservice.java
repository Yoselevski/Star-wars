package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BroadCastMessage;
import bgu.spl.mics.application.messages.LeiaFinish;
import bgu.spl.mics.application.messages.TerminateBroadCast;
import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;
    private long currentTime;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        currentTime = 0;
    }

    @Override
    protected void initialize() {
            subscribeBroadcast(BroadCastMessage.class, callBackAttackEvent->{
                LeiaFinish LeiaFinshMessage = new LeiaFinish();
                AttackEvent AttackToPush;
               int sizeArray=attacks.length;
               for (int i=0; i<sizeArray;i++) {
                   AttackToPush = new AttackEvent(attacks[i]);
                  sendEvent(AttackToPush);
               }
               sendBroadcast(LeiaFinshMessage);
            });

            subscribeBroadcast(TerminateBroadCast.class, callBackTerminate->{
                terminate();
                diary.SetLeiaTerminate();
            });

        sendBroadcast(new BroadCastMessage(0));
    }

        public void loading (Attack[] attacks){
            for (int i = 0; i < attacks.length; i++)
                this.attacks[i] = attacks[i];
        }
    }
