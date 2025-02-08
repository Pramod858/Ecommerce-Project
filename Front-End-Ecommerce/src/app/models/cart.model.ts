import { CartItem } from "./cartItem.model";

export class Cart {
    id: number;
    cartItems: CartItem[];
    totalPrice: number;

    constructor(id: number, cartItems: CartItem[], totalPrice: number) {
        this.id = id;
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }
}