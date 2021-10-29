package com.interfacesgraphiqueset3d.tp5;

import com.badlogic.gdx.math.Vector3;

public class Lampe {
    
    public final Vector3 pos = new Vector3();
    public float intensite = 0;
    public final Vector3 couleur = new Vector3();
    
    /** Constructor, sets the starting position of the ray and the direction.
    * 
    * @param pos The position of the lamp
    * @param intensite The intensity of the lamp 
    * @param couleur The color of the lamp*/
    public Lampe (Vector3 pos, float intensite, Vector3 couleur) {
		this.pos.set(pos);
        this.intensite = intensite;
        this.couleur.set(couleur);
		
	}

}
