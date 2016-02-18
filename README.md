
# ColorTime

Written as a test for the ReflectiveSettings library component, 
all settings menus are generated using reflection (At the cost of performance)

## Description

ColorTime is pulled into 2 components, A Painter and a Sampler

## Sampler

```java
    @Override
    public int getColor(long delta)
```

## Painter

```java
    @Override
    public void paint(final SurfaceHolder holder, final int color, long delta)
```

## Eventual Changes

An extension of this, would be to "Chain" painters and samplers or to create modifiers to allow for modification of the color after the fact. 

`Get Color → Modify Color → Modify Color → Paint → Modify SurfaceHolder`
