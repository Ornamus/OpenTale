function init() {
    attack.setBounds(200, 140);
    attack.setTimeLength(8);
    fire = new SpriteSheet(13, 14, 2, 1, "attacks/fireball");
    attack.doOnLoop(.2, function() {
        var moveSpeed = Utils.randomNumber(2, 4);
        var animSpeed = 0;
        if (moveSpeed == 4) animSpeed = 2;
        else if (moveSpeed == 3) animSpeed = 3;
        else if (moveSpeed == 2) animSpeed = 4;
        var a = new Animation(animSpeed, fire.get(0, 0), fire.get(1, 0));
        var p = new Projectile(((Main.WIDTH / 2) - (attack.getWidth() / 2)) + Utils.randomNumber(0, attack.getWidth() - 20) + 10, 250, a);
        p.moveAtAngle(0);
        p.setMoveSpeed(moveSpeed);
        attack.spawnProjectiles(p);
     });
}

function start() {}

function tick() {}