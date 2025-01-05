import {Scene} from "phaser"


//Clojurescript is overall great, but it does not have
//good support for extending JS classes. This is
//unfortunately the easiest solution. An alternative
//would be to do ES5 extension, but it's also quite
//clunky
export class SceneExt extends Scene {

    constructor(conf, p, c, u){
        super(conf);
        this.p = p;
        this.c = c;
        this.u = u;
    }

    preload (){
        this.p(this);
    }

    create(){
        this.c(this);
    }

    update(time,delta){
        this.u(this,time,delta);
    }
}