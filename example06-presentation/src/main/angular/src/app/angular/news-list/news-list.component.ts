import { Component, Input ,Output,EventEmitter } from '@angular/core';
import { User } from 'src/app/User';
import { News } from '../../news';
import { NewsService } from '../news.service';
export class CheckBox {
  key: '';
  status: boolean
}
@Component({ 
  selector: 'wt2-news-list',
  templateUrl: './news-list.component.html',
  styleUrls: ['./news-list.component.sass'],
  providers: [NewsService]
})
export class NewsListComponent {

  @Output()
  public upd = new EventEmitter();

  constructor(protected newsService: NewsService) {
  }

  @Input()
  public news: News[] = [];

  public noteId:string=''

  public userList:CheckBox[]=[]
  public usernameOn:string[]=[]
  

  get reversedNews(): News[] {
    return this.news.slice().reverse();
  }
  
  doDelete(a): void {
    this.newsService.deleteNote(a.id).subscribe({
      next: () => {
        console.log("aa")
        this.upd.emit();
        console.log("bb")
      },
      error: console.error
    });
  }

  doAssign(a): void {
    console.log(a)
    this.userList=[]
    this.noteId = a.id
    this.newsService.getUserList(a.id).subscribe({
      next: UserList => {
        console.log("eee",a)
        // this.usernameOn= UserList.usernameAll
        UserList.usernameAll.forEach(element => {
          this.userList.push({
            key:element,
            status:this.setCheck(element,UserList.usernameOn)
          })
        });
        console.log('this.userList',this.userList)
        this.usernameOn = UserList.usernameOn
      },
      error: console.error
    });
  }

  setCheck(u,usernameOn):boolean{
    console.log("this.userListon",usernameOn)
    console.log("user",u)
    return usernameOn.indexOf(u)>-1
  }
  doCheck(v):void{
    console.log(v)
    var index = this.usernameOn.indexOf(v)
    if(index>-1){
      this.usernameOn.splice(index,1)
    }else{
      this.usernameOn.push(v)
    }
  }
  doEdit(n):void{
    console.log("edit")
    this.upd.emit();
    console.log("edi0t")
  }
  doUpdate():void{
    console.log("this.usernameOn",this.usernameOn)

    this.newsService.updateUsernameOn(this.noteId,this.usernameOn).subscribe({
      next: () => {
        this.userList=[]
      },
      error: console.error
    });

  }
}
