import { PaymentStatus } from "./payment-status.enum";
import { User } from "./user.model";

export class Payment {
    id: number;
    user: User;
    amount: number;
    paymentMethod: string;
    paymentStatus: PaymentStatus;
    cardNumber: string;
    cardCVV: string;
    cardExpiry: string;
    cardholderName: string;

    constructor(
        id: number,
        user: User,
        amount: number,
        paymentMethod: string,
        paymentStatus: PaymentStatus,
        cardNumber: string,
        cardCVV: string,
        cardExpiry: string,
        cardholderName: string
    ) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.cardExpiry = cardExpiry;
        this.cardholderName = cardholderName;
    }
}
