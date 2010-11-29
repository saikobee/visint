public class WavierFunc
extends WaveFunc {
    public WavierFunc() {
        this.scale  =  1f;
        this.period = 15f;
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

    private float normalX(float x) {
        float fPrimeG = (float) (scale * Math.sin(freq * x));
        float fGPrime = (float) (freq * scale * x * Math.cos(freq * x));

        return fPrimeG + fGPrime;
    }
    private float normalZ(float z) {
        float fPrimeG = (float) (scale * Math.sin(freq * z));
        float fGPrime = (float) (freq * scale * z * Math.cos(freq * z));

        return fPrimeG + fGPrime;
    }

    public float[] normalAt(float x, float z) {
        return new float[] {
            normalX(x), 0, normalZ(z)
        };
    }
}
