
import createGlobe from 'https://cdn.skypack.dev/cobe';

export function initGlobe(canvas) {
    let phi = 0;

    // Default size logic (can be overridden by canvas attribute or CSS)
    // We'll rely on the canvas verify size logic in the library, 
    // but usually we need to set width/height.

    // Configuration for "Nano" / High-Tech feel (Emerald + Slate)
    const onRender = createGlobe(canvas, {
        devicePixelRatio: 2,
        width: canvas.offsetWidth * 2,
        height: canvas.offsetHeight * 2,
        phi: 0,
        theta: 0,
        dark: 0, // 0 = Light Mode (since site is mostly light), 1 = Dark
        diffuse: 1.2,
        mapSamples: 16000,
        mapBrightness: 6,
        baseColor: [1, 1, 1], // White/Light base
        markerColor: [0.06, 0.73, 0.5], // Emerald-500 (#10B981)
        glowColor: [0.94, 0.96, 0.98], // Slate-50/100ish
        markers: [
            // Austin, TX (approx)
            { location: [30.2672, -97.7431], size: 0.1 },
            // New York
            { location: [40.7128, -74.0060], size: 0.05 },
            // San Francisco
            { location: [37.7749, -122.4194], size: 0.05 },
            // Los Angeles
            { location: [34.0522, -118.2437], size: 0.05 },
            // Chicago
            { location: [41.8781, -87.6298], size: 0.05 },
        ],
        onRender: (state) => {
            // Called on every animation frame.
            // state.phi = current rotation
            state.phi = phi
            phi += 0.003
        },
    });

    // Handle resizing
    const resizeObserver = new ResizeObserver(() => {
        canvas.width = canvas.offsetWidth * 2;
        canvas.height = canvas.offsetHeight * 2;
    });
    resizeObserver.observe(canvas);

    return {
        destroy: () => {
            resizeObserver.disconnect();
            // cobe doesn't expose a destroy method directly in simple mode but 
            // usually relies on canvas removal or stopping the loop if managed.
            // For this simple usage, we just let it run. 
        }
    };
}
