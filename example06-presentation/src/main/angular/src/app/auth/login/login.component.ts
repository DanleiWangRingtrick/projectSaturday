import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'wt2-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent {

  @Input()
  public authService: AuthService;

  @Output()
  public loggedIn = new EventEmitter<void>();

  public username: string = "";
  public password: string = "";
  public isLogin: string = "1";
  public errorMessage: string;
  

  login(e: Event) {
    e.preventDefault();
    this.errorMessage = null;
    if (this.canLogin) {
      if(this.isLogin==='1')
      {
        this.authService.login(this.username, this.password).subscribe({
            next: () => this.loggedIn.emit(),
            error: () => this.errorMessage = 'Failed to login'
          });
      }else{
        this.authService.addUser(this.username, this.password).subscribe({
          next: () => this.loggedIn.emit(),
          error: () => this.errorMessage = 'Failed to register'
        });
      }
    }
  }

  get canLogin(): boolean {
    return this.username.trim() !== '' && this.password.trim() !== '';
  }
}
