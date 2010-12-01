public class WavierFunc
extends WaveFunc {
    public WavierFunc() {
        this.scale  =  0.25f;
        this.period = 30.00f;
        calcFreq();
    }

    public float valueAt(float x, float z) {
        return valueX(x) + valueZ(z);
    }

    private float valueX(float x) {
        return (float) (scale * x * Math.sin(freq * x));
    }
    private float valueZ(float z) {
        return (float) (scale * z * Math.sin(freq * z));
    }

    public float xPartial(float x) {
        float fPrimeG = (float) (scale * Math.sin(freq * x));
        float fGPrime = (float) (freq * scale * x * Math.cos(freq * x));

        return fPrimeG + fGPrime;
    }
    public float zPartial(float z) {
        float fPrimeG = (float) (scale * Math.sin(freq * z));
        float fGPrime = (float) (freq * scale * z * Math.cos(freq * z));

        return fPrimeG + fGPrime;
    }
}
