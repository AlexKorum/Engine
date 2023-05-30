#version 400 core

in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

layout(location = 0) out vec4 fragColor;

uniform vec3 lightColor;

uniform vec3 materialColor;
uniform float reflectivity;
uniform float shineDamping;


void main(void){
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.0);
    vec3 diffuse = brightness * normalize(lightColor);

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamping);
    vec3 finalSpecular = dampedFactor * lightColor;

    vec3 color = vec3(diffuse) * materialColor + finalSpecular + vec3(0.1f);
    color = pow(color, vec3(0.454545));
    fragColor = vec4(color, 1.0);
}