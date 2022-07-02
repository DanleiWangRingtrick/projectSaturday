export class User {
  usernameAll: [];
  usernameOn: []

  static fromObject(object: any): User {
    const n = new User();
    n.usernameAll = object.usernameAll;
    n.usernameOn = object.usernameOn;
    return n;
  }
}
