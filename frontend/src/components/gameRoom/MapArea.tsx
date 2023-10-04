import { useCallback } from 'react';

type Props = {
  height?: number;
  width?: number;
  src: string;
  alt: string;
  value: number;
  onClickArea?: (landId: number) => void;
};

const MapArea = ({ height, width, src, alt, value, onClickArea }: Props) => {
  const onClick = useCallback(() => {
    onClickArea ? onClickArea(value) : {};
    console.log(value);
  }, [onClickArea, value]);

  return (
    <button onClick={onClick}>
      <img
        src={src}
        alt={alt}
        style={{
          width: `${width}px`,
          height: `${height}px`,
        }}
      />
    </button>
  );
};

export default MapArea;
