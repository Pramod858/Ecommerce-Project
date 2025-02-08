import { User } from "./user.model";

export class Address {
    id: number;
    user: User;
    streetAddress: string;
    city: string;
    state: string;
    zipCode: string;
    country: string;

    constructor(id: number, user: User, streetAddress: string, city: string, state: string, zipCode: string, country: string) {
        this.id = id;
        this.user = user;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
}