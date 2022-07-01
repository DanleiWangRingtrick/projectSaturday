export class News {
  id:Number;
  publishedOn: Date;
  headline: string;
  content: string;

  static fromObject(object: any): News {
    const n = new News();
    n.id = object.id;
    n.headline = object.headline;
    n.content = object.content;
    n.publishedOn = new Date(object.publishedOn);
    return n;
  }
}
